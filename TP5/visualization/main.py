from ovito_visualization import export_to_ovito
import pandas as pd
import numpy as np

from plots import plot_data


def ejA():
    static_file = "static.txt"
    dynamic_file = "dynamic.txt"
    L = 70
    W = 20
    D = 3

    export_to_ovito(static_file, dynamic_file,L,W,D,"ovito.dump")

    flow_df = pd.read_csv("flow.txt", names=["t"])
    flow_times = flow_df["t"].tolist()
    actual_flow = []
    n_s = []
    window = 5
    #para unos mil segundos, probar la ventana de 10
    #superponer para los diferentes ws
    print(flow_times)
    for i in range(len(flow_times) - window):
        actual_flow.append(window/(flow_times[i + window - 1] - flow_times[i]))
        print((flow_times[i + window - 1] - flow_times[i]))
        n_s.append(window+i)

    plot_data(n_s, actual_flow, "Q vs N", "N", "Q")

if __name__ == '__main__':
    ejA()
