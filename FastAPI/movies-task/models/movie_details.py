from pydantic import BaseModel
from typing import List, Optional


class Recommendation(BaseModel):
    name: str  
    description: str
    wUrl: str
    yID: str

class MovieDetails(BaseModel):
    id: int
    title: str 
    overview: str
    release_date: str
    budget: int
    revenue: int
    runtime: int
    vote_average: float
    vote_count: int
    production_countries: List[str]
    genres: List[str]
    recommendations: List[Recommendation]
