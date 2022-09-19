import pandas as pd
import numpy as np


def parse_data(static_file, dynamic_file):
    static_df = pd.read_csv(static_file, skiprows=2, sep=" ", names=["radius","mass"])
    dfs = []
    with open(dynamic_file, "r") as results:
        current_time = float(next(results))
        current_frame = []
        for line in results:
            tokens = list(map(lambda t: float(t), line.split()))
            if len(tokens) > 1:
                current_frame.append(tokens)
            elif len(tokens) == 1: #time
                df = pd.DataFrame(np.array(current_frame), columns=["x","y","z","vx","vy"])
                dfs.append(tuple([tokens[0],pd.concat([df,static_df],axis=1)]))
                current_frame = []
    return dfs
