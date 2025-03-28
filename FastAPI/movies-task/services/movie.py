from fastapi import HTTPException
from config import API_KEY_TMDB # themoviedb
from config import API_KEY_TASTEDIVE # tastedrive
from models.movie_response import Movie, MovieSearchResponse
from typing import Optional
import httpx
import asyncio
import matplotlib.pyplot as plt
from collections import Counter
from matplotlib.backends.backend_agg import FigureCanvasAgg as FigureCanvas
import io
import base64

all_genre_names = None

async def fetch_movies_data(
    genre: Optional[str] = None,
    year: Optional[int] = None,
    actor: Optional[str] = None,
    min_rating: Optional[float] = None,
    max_rating: Optional[float] = None,
    language: Optional[str] = None,
    min_votes: int = 10,
    sort_by: str = "popularity.desc"
):
    global all_genre_names
    if not all_genre_names:
        all_genre_names = await get_all_genre_names()

    genre_id, actor_id = await asyncio.gather(
        get_genre_id(genre),
        get_actor_id(actor),
    )

    all_movies = await get_movies(genre_id, year, actor_id, min_rating, max_rating, language, min_votes, sort_by)

    if all_movies:
        avg_rating = sum(movie.vote_average for movie in all_movies) / len(all_movies)
        best_movie = max(all_movies, key=lambda movie: movie.vote_average)
        common_language, (avg_runtime, avg_revenue) = await asyncio.gather(
            get_most_common([movie.original_language for movie in all_movies]),
            get_average_runtime_revenue([movie.id for movie in all_movies]),
        )
    else:
        avg_rating = 0
        best_movie = None
        common_language = "N/A"
        avg_runtime = 0
        avg_revenue = 0

    plot = await plot_genre_distribution(all_movies)

    return MovieSearchResponse(
        movies=all_movies,
        results_count=len(all_movies),
        best_movie=best_movie,
        avg_rating=avg_rating,
        common_language=common_language,
        avg_runtime=avg_runtime,
        avg_revenue=avg_revenue,
        plot=plot
    )



async def plot_genre_distribution(movies):
    global all_genre_names

    all_genres = []
    for movie in movies:
        for genre_id in movie.genre_ids:
            genre_name = all_genre_names.get(genre_id, f"Unknown ({genre_id})")
            all_genres.append(genre_name)
    
    genre_counts = Counter(all_genres)
    genres = list(genre_counts.keys())
    counts = list(genre_counts.values())

    fig, ax = plt.subplots(figsize=(12, 6))
    ax.barh(genres, counts, color='skyblue')
    ax.set_xlabel('Films number')
    ax.set_ylabel('Genre')
    ax.set_title('Films number in each genre')

    buf = io.BytesIO()
    canvas = FigureCanvas(fig)
    canvas.print_png(buf)
    buf.seek(0)

    img_base64 = base64.b64encode(buf.read()).decode('utf-8')
    return img_base64


async def get_actor_id(actor):
    if not actor:
        return None
    
    async with httpx.AsyncClient() as client:
        response = await client.get(f"https://api.themoviedb.org/3/search/person?api_key={API_KEY_TMDB}&query={actor}")
        response.raise_for_status()

    if response.status_code != 200:
        raise HTTPException(status_code=response.status_code, detail="Failed to fetch actor data from API.")
    
    actor_data = response.json()
    
    if not actor_data.get("results"):
        raise HTTPException(status_code=404, detail=f"Actor '{actor}' not found.")


    return actor_data["results"][0]["id"]


async def get_genre_id(genre):
    if not genre:
        return None
    
    async with httpx.AsyncClient() as client:
        genre_response = await client.get(f"https://api.themoviedb.org/3/genre/movie/list?api_key={API_KEY_TMDB}&language=en-US")
        

    if genre_response.status_code != 200:
        raise HTTPException(status_code=genre_response.status_code, detail="Failed to fetch genre data from API.")
    
    genre_data = genre_response.json()
    
    if not genre_data.get("genres"):
        raise HTTPException(status_code=404, detail=f"Genre '{genre}' not found.")

    if "genres" in genre_data:
        for g in genre_data["genres"]:
            if g["name"].lower() == genre.lower():
                return g["id"]
            
    raise HTTPException(status_code=404, detail=f"Genre '{genre}' not found.")


async def get_movies(genre_id, year, actor_id, min_rating, max_rating, language, min_votes, sort_by):
    url = f"https://api.themoviedb.org/3/discover/movie?api_key={API_KEY_TMDB}&sort_by={sort_by}"

    if year:
        url += f"&primary_release_year={year}"
    if genre_id:
        url += f"&with_genres={genre_id}"
    if actor_id:
        url += f"&with_cast={actor_id}"
    if min_rating:
        url += f"&vote_average.gte={min_rating}"
    if max_rating:
        url += f"&vote_average.lte={max_rating}"
    if language:
        url += f"&language={language}"
    if min_votes:
        url += f"&vote_count.gte={min_votes}"

    all_movies = []
    page = 1
    while len(all_movies) < 100:
        paginated_url = f"{url}&page={page}"
        try:
            async with httpx.AsyncClient() as client:
                response = await client.get(paginated_url)

            if response.status_code != 200:
                raise HTTPException(status_code=response.status_code, detail="Failed to fetch movie data from API.")
            
            data = response.json()
            
            movies = data.get("results", [])
            if not movies:
                break

            movie_objects = [
                Movie(
                    id=movie["id"],
                    title=movie["title"],
                    vote_average=movie["vote_average"],
                    release_date=movie["release_date"],
                    overview=movie["overview"],
                    original_language=movie["original_language"],
                    genre_ids=movie["genre_ids"],
                    vote_count=movie["vote_count"],
                    adult=movie["adult"]
                )
                for movie in movies
            ]

            all_movies.extend(movie_objects)
            page += 1

        except httpx.RequestError as e:
            raise HTTPException(status_code=500, detail=f"An error occurred while making the request: {str(e)}")
        except httpx.HTTPStatusError as e:
            raise HTTPException(status_code=e.response.status_code, detail=f"API error: {str(e)}")
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Unexpected error occurred: {str(e)}")

    return all_movies


async def get_most_common(lst):
    if not lst:
        return "N/A"
    return max(set(lst), key=lst.count)


async def get_average_runtime_revenue(movie_ids):
    async with httpx.AsyncClient() as client:
        tasks = [client.get(f"https://api.themoviedb.org/3/movie/{movie_id}?api_key={API_KEY_TMDB}") for movie_id in movie_ids]
        
        try:
            responses = await asyncio.gather(*tasks)
        except httpx.RequestError as exc:
            raise HTTPException(status_code=500, detail=f"Request error occurred: {str(exc)}")
        except Exception as exc:
            raise HTTPException(status_code=500, detail=f"An unexpected error occurred: {str(exc)}")

    total_runtime = 0
    total_revenue = 0
    count = 0

    for response in responses:
        if response.status_code == 200:
            try:
                movie_data = response.json()
                if "runtime" in movie_data and movie_data["runtime"] is not None:
                    total_runtime += movie_data["runtime"]
                    total_revenue += movie_data["revenue"]
                    count += 1
            except ValueError as exc:
                raise HTTPException(status_code=500, detail=f"Error decoding response: {str(exc)}")
            except KeyError as exc:
                raise HTTPException(status_code=500, detail=f"Missing expected field in the response: {str(exc)}")


    return (total_runtime / count if count > 0 else 0, 
            total_revenue / count if count > 0 else 0)

async def get_all_genre_names():
    async with httpx.AsyncClient() as client:
        response = await client.get(f"https://api.themoviedb.org/3/genre/movie/list?api_key={API_KEY_TMDB}&language=en-US")
        data = response.json()

    if response.status_code != 200:
        raise HTTPException(status_code=response.status_code, detail="Failed to fetch genres data from API.")
    
    data = response.json()

    if not data.get("genres"):
        raise HTTPException(status_code=404, detail="Genres not found in API response.")
                            
    genre_names = {}
    if "genres" in data:
        for genre in data["genres"]:
            genre_names[genre["id"]] = genre["name"]
    
    return genre_names
