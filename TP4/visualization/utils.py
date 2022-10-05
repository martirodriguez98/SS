from pandas import DataFrame
import pandas as pd
import numpy as np
import datetime

def parse_data(static_file, dynamic_file):
    dfs = []
    static_df = pd.read_csv(static_file, skiprows=0, sep=",", names=["name","radio","mass","x","y","vx","vy"], usecols=["name","radio","mass"])
    with open(dynamic_file, "r") as results:
        current_time = float(next(results))
        current_frame = []
        for line in results:
            tokens = list(map(lambda t: float(t),line.split(',')))
            if len(tokens) > 1:
                current_frame.append(tokens)
            elif len(tokens) == 1: #time
                df = pd.DataFrame(np.array(current_frame), columns=["id","x","y","vx","vy"])
                dfs.append(tuple([tokens[0],pd.concat([df,static_df],axis=1)]))
                current_frame = []
    return dfs

def get_spaceship_distance(data: DataFrame, initial_date:datetime):
    dates = []
    distances = []
    for df in data:
        date = initial_date + datetime.timedelta(seconds=df[0])
        dates.append(date)
        sx = 0
        sy = 0
        vx = 0
        vy = 0
        rv = 0
        for row in df[1].iterrows():
            if row[1]['name'] == 'SPACESHIP':
                sx = row[1]['x']
                sy = row[1]['y']
            if row[1]['name'] == 'VENUS':
                vx = row[1]['x']
                vy = row[1]['y']
                print(row[1])
                rv = row[1]['radio']
        distances.append(np.sqrt((sx - vx) ** 2 + (sy - vy) ** 2) - rv )
    print(distances)
    return [dates,distances]

