import sys
from typing import Dict

import pandas as pd
import numpy as np


from visualization.ovito_visualization import export_to_ovito
from visualization.plots import plot_order_coeff, plot_avg_order

if __name__ == '__main__':
    # argv = sys.argv
    # if len(argv) == 3:
    #     static_file = argv[1]
    #     dynamic_file = argv[2]
    # else:
    #     raise ValueError(f'Please provide file with information.')
    static_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt"
    dynamic_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt"
    L = 5
    n = 1
    N = 300
    # export_to_ovito(static_file, dynamic_file,L)

    noises_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/noises.txt"
    noises = pd.read_csv(noises_file, names=["n"])
    coeff_df_list = []
    for i in range(len(noises)):
        coeff_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/coeff_{i}.txt'
        coeff_df = pd.read_csv(coeff_file, sep=" ", names=["it", "coeff"])
        coeff_df_list.append(coeff_df)

    #averge order for different noises
    iteration = 9000
    average_order = []
    std_dev = []

    reduced_coeff_list = []
    reduced_noises = [0.0, 0.4, 0.6, 1.2, 3, 6]

    for i,c in enumerate(coeff_df_list):
        average_order.append(np.average(c["coeff"][iteration:]))
        std_dev.append(np.std(c["coeff"][iteration:]))
        if round(noises.values[i][0],1) in reduced_noises:
            reduced_coeff_list.append({"it": c["it"],"coeff":c["coeff"]})

    plot_avg_order(average_order, std_dev, noises, iteration)

    plot_order_coeff(reduced_coeff_list, reduced_noises, L, N)

