from fastapi import HTTPException
import httpx
from config import API_KEY_TMDB # themoviedb
from config import API_KEY_TASTEDIVE # tastedrive
from models.movie_details import MovieDetails, Recommendation


async def fetch_movie_details_and_recommendations(movie_id: int):
    movie_details = await get_movie_details(movie_id)

    movie_title = movie_details.get("title")
    recommendations = await get_movie_recommendations(movie_title)

    movie = MovieDetails(
        id=movie_details["id"],
        overview=movie_details["overview"],
        title=movie_details["title"],
        release_date=movie_details["release_date"],
        budget=movie_details["budget"],
        revenue=movie_details["revenue"],
        runtime=movie_details["runtime"],
        vote_average=movie_details["vote_average"],
        vote_count=movie_details["vote_count"],
        production_countries=[country["name"] for country in movie_details["production_countries"]],
        genres=[genre["name"] for genre in movie_details["genres"]],
        recommendations=recommendations,
    )
    
    return movie


async def get_movie_details(movie_id):
    async with httpx.AsyncClient() as client:
        response = await client.get(f"https://api.themoviedb.org/3/movie/{movie_id}?api_key={API_KEY_TMDB}")

    if response.status_code != 200:
        raise HTTPException(status_code=response.status_code, detail="Failed to fetch movie details from API.")
    
    movie_data = response.json()
    if not movie_data or "id" not in movie_data:
        raise HTTPException(status_code=404, detail="Movie not found or missing necessary data.")
    
    return movie_data

async def get_movie_recommendations(movie_title: str):
    async with httpx.AsyncClient() as client:
        response = await client.get(f"https://tastedive.com/api/similar?q={movie_title}&type=movie&info=1&k={API_KEY_TASTEDIVE}")
    
    if response.status_code != 200:
        raise HTTPException(status_code=response.status_code, detail="Failed to fetch movie recommendations from the API.")
    
    recommendations_data = response.json()
    if not recommendations_data or "similar" not in recommendations_data or "results" not in recommendations_data["similar"]:
        raise HTTPException(status_code=404, detail="Recommendations not found or missing necessary data.")
    
    recommendations = []
    for rec in recommendations_data.get("similar", {}).get("results", []):
        recommendations.append(
            Recommendation(
                name=rec["name"],
                description=rec["description"],
                wUrl=rec["wUrl"],
                yID=rec["yID"]
            )
        )

    return recommendations