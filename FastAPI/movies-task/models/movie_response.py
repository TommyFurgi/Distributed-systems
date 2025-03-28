from pydantic import BaseModel
from typing import List, Optional

class Movie(BaseModel):
    id: int
    title: str
    vote_average: float
    release_date: str
    overview: str
    original_language: str
    genre_ids: List[int]
    vote_count: int
    adult: bool

class MovieSearchResponse(BaseModel):
    movies: list[Movie]
    results_count: int
    best_movie: Optional[Movie] = None
    avg_rating: float
    common_language: str
    avg_runtime: float
    avg_revenue: float
    plot: str