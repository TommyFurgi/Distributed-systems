from fastapi import FastAPI, HTTPException
from enum import Enum
from pydantic import BaseModel
from fastapi import Body, FastAPI, status
from fastapi.responses import JSONResponse
from typing import List, Dict

app=FastAPI()

polls: Dict[int, Dict] = {}
votes: Dict[int, Dict[int, int]] = {}
poll_id_counter = 1

class Poll(BaseModel):
    question: str
    options: List[str]

class Vote(BaseModel):
    option_index: int

@app.get("/polls/")
def get_all_polls():
    return {"polls": polls}

@app.get("/polls/{poll_id}")
async def get_poll_results(poll_id: int):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    return polls[poll_id]

@app.post("/polls/", status_code=201)
def create_poll(poll: Poll):
    global poll_id_counter
    poll_id = poll_id_counter
    poll_id_counter += 1 
    
    polls[poll_id] = {"question": poll.question, "options": poll.options, "votes": [0] * len(poll.options)}
    votes[poll_id] = {}
    return {"message": "Poll created successfully", "poll_id": poll_id, "poll": polls[poll_id]}

@app.put("/polls/{poll_id}")
async def update_poll(poll_id: int, updated_poll: Poll):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    polls[poll_id] = {
        "question": updated_poll.question,
        "options": updated_poll.options,
        "votes": [0] * len(updated_poll.options)
    }
    
    return {"message": "Poll updated successfully", "poll": polls[poll_id]}

@app.delete("/polls/{poll_id}")
async def delete_poll(poll_id: int):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    del polls[poll_id]
    return {"message": "Poll deleted successfully"}

@app.get("/polls/{poll_id}/vote/")
async def get_poll_votes(poll_id: int):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    return {"poll_id": poll_id, "votes": polls[poll_id]["votes"]}

@app.post("/polls/{poll_id}/vote/")
async def cast_vote(poll_id: int, vote: Vote):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    if vote.option_index < 0 or vote.option_index >= len(polls[poll_id]["options"]):
        raise HTTPException(status_code=400, detail="Invalid option index")
    
    polls[poll_id]["votes"][vote.option_index] += 1
    return {"message": "Vote cast successfully", "poll": polls[poll_id]}


@app.get("/polls/{poll_id}/vote/{vote_id}")
async def get_vote(poll_id: int, vote_id: int):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    if poll_id not in votes or vote_id not in votes[poll_id]:
        raise HTTPException(status_code=404, detail="Vote not found")
    
    return {"poll_id": poll_id, "vote_id": vote_id, "option_index": votes[poll_id][vote_id]}


@app.put("/polls/{poll_id}/vote/{vote_id}")
async def change_vote(poll_id: int, vote_id: int, vote: Vote):
    if poll_id not in polls:
        raise HTTPException(status_code=404, detail="Poll not found")
    
    if vote_id not in votes[poll_id]:
        raise HTTPException(status_code=404, detail="Vote not found")

    if vote.option_index < 0 or vote.option_index >= len(polls[poll_id]["options"]):
        raise HTTPException(status_code=400, detail="Invalid option index")

    previous_option = votes[poll_id][vote_id]
    polls[poll_id]["votes"][previous_option] -= 1
    polls[poll_id]["votes"][vote.option_index] += 1

    votes[poll_id][vote_id] = vote.option_index

    return {"message": "Vote updated successfully", "poll": polls[poll_id]}