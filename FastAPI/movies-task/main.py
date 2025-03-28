from fastapi import FastAPI, Request, HTTPException
from fastapi.responses import HTMLResponse, JSONResponse
from fastapi.templating import Jinja2Templates
from fastapi.exceptions import RequestValidationError
from fastapi import FastAPI, Query
from typing import Optional
from services.movie_details import fetch_movie_details_and_recommendations
from services.movie import get_all_genre_names, fetch_movies_data
import httpx

app = FastAPI()
templates = Jinja2Templates(directory="./templates")

@app.get("/movies", response_class=HTMLResponse)
async def form(request: Request):
    await get_all_genre_names()

    return templates.TemplateResponse("index.html", {"request": request})

@app.get("/movies/search", response_class=JSONResponse)
async def search_movies_json(
    genre: Optional[str] = Query(None),
    year: Optional[int] = Query(None),
    actor: Optional[str] = Query(None),
    min_rating: Optional[float] = Query(None),
    max_rating: Optional[float] = Query(None),
    language: Optional[str] = Query(None),
    min_votes: Optional[int] = Query(10),
    sort_by: str = Query("popularity.desc")
):
    
    movies_data = await fetch_movies_data(
        genre=genre,
        year=year,
        actor=actor,
        min_rating=min_rating,
        max_rating=max_rating,
        language=language,
        min_votes=min_votes,
        sort_by=sort_by
    )

    return JSONResponse(content=movies_data.model_dump())

@app.get("/movies/search/view", response_class=HTMLResponse)
async def search_movies_html(
    request: Request,
    genre: Optional[str] = Query(None),
    year: Optional[int] = Query(None),
    actor: Optional[str] = Query(None),
    min_rating: Optional[float] = Query(None),
    max_rating: Optional[float] = Query(None),
    language: Optional[str] = Query(None),
    min_votes: Optional[int] = Query(10),
    sort_by: str = Query("popularity.desc"),
):
    response = await fetch_movies_data(
        genre=genre,
        year=year,
        actor=actor,
        min_rating=min_rating,
        max_rating=max_rating,
        language=language,
        min_votes=min_votes,
        sort_by=sort_by
    )

    return templates.TemplateResponse("results.html", {
        "request": request,
        "response": response,
    })

# Example: Inception 27205
@app.get("/movies/{movie_id}", response_class=JSONResponse)
async def movie_details_json(movie_id: int):
    movie = await fetch_movie_details_and_recommendations(movie_id)
    
    return JSONResponse(content={
        "movie": movie.model_dump(), 
    })

@app.get("/movies/{movie_id}/view", response_class=HTMLResponse)
async def movie_details_html(request: Request, movie_id: int):
    movie = await fetch_movie_details_and_recommendations(movie_id)

    return templates.TemplateResponse(
        "movie.html", 
        {"request": request, "movie": movie}
    )



@app.exception_handler(httpx.HTTPStatusError)
async def http_error_handler(request: Request, exc: httpx.HTTPStatusError):
    error_message = f"Error while communicating with the external API. Code: {exc.response.status_code}, Details: {str(exc)}"
    return templates.TemplateResponse("error.html", {"request": request, "error_message": error_message}, status_code=exc.response.status_code)

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError):
    error_message = f"Invalid input data: {str(exc)}"
    return templates.TemplateResponse("error.html", {"request": request, "error_message": error_message}, status_code=422)

@app.exception_handler(HTTPException)
async def http_exception_handler(request: Request, exc: HTTPException):
    return templates.TemplateResponse(
        "error.html",
        {"request": request, "error_message": exc.detail},
        status_code=exc.status_code
    )