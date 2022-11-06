from ovito_visualization import export_to_ovito
import pandas as pd
import numpy as np

from plots import plot_data, plot_many_data, plot_error


def ejA():
    static_file = "static.txt"
    dynamic_file = "dynamic.txt"
    L = 70
    W = 20
    D = 3

    export_to_ovito(static_file, dynamic_file, L, W, D, "ovito.dump")

    flow_df = pd.read_csv("flow.txt", names=["t"])
    flow_times = flow_df["t"].tolist()
    actual_flow = []
    n_s = []
    window = 5
    # para unos mil segundos, probar la ventana de 10
    # superponer para los diferentes ws
    print(flow_times)
    for i in range(len(flow_times) - window):
        actual_flow.append(window / (flow_times[i + window - 1] - flow_times[i]))
        print((flow_times[i + window - 1] - flow_times[i]))
        n_s.append(window + i)

    plot_data(n_s, actual_flow, "Q vs N", "N", "Q")


def manyW():
    w_s = [5.0, 10.0, 15.0, 20.0, 30.0, 50.0]
    w_text = [5, 10, 15, 20, 30, 50]
    flow_df = []
    all_flows = []
    window = 5
    n_s = []
    std_dev = []
    mean = []
    for i,w in enumerate(w_s):
        file = f'flow_{w}.txt'
        flow_df.append(pd.read_csv(file, names=["t"]))
        flow_times = flow_df[i]["t"].tolist()
        actual_flow = []
        for j in range(len(flow_times) - window):
            actual_flow.append(window / (flow_times[j + window - 1] - flow_times[j]))

        all_flows.append(actual_flow)
        n_s.append(np.arange(window,len(actual_flow)+window,1))
        std_dev.append(np.std(actual_flow))
        mean.append(np.mean(actual_flow))
        #This plots singular plot for every w
        plot_data(n_s[i], actual_flow, f'Q vs N for w = {w_text[i]}', "N", "Q (partículas/s)")

    # plot_many_data(n_s[:3],all_flows[:3],w_text[:3],"Q vs N", "N","Q (partículas/s)","W (Hz)")
    # plot_many_data(n_s[3:],all_flows[3:],w_text[3:],"Q vs N", "N","Q (partículas/s)","W (Hz)")

    # plot_error(w_text,mean,std_dev,"Q error for different w","W (Hz)","Q (partículas/s)")
def manyD():
    d_s = [3,4,5,6]
    flow_df = []
    all_flows = []
    window = 10
    n_s = []
    std_dev = []
    mean = []
    for i,d in enumerate(d_s):
        file = f'flow_w20_{d}.txt'
        flow_df.append(pd.read_csv(file, names=["t"]))
        flow_times = flow_df[i]["t"].tolist()
        actual_flow = []
        for j in range(len(flow_times) - window):
            actual_flow.append(window / (flow_times[j + window - 1] - flow_times[j]))

        all_flows.append(actual_flow)
        n_s.append(np.arange(window,len(actual_flow)+window,1))
        std_dev.append(np.std(actual_flow))
        mean.append(np.mean(actual_flow))
        #This plots singular plot for every w
        plot_data(n_s[i], actual_flow, f'Q vs N for d = {d}', "N", "Q (partículas/s)")

    plot_many_data(n_s,all_flows,d_s,"Q vs N", "N","Q (partículas/s)","D (cm)")

def c():
    

if __name__ == '__main__':
    # manyW()
    manyD()
