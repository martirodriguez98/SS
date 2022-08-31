
import pandas as pd
import numpy as np

from visualization.ovito_visualization import export_to_ovito
from visualization.plots import plot_order_coeff, plot_one_avg_order, plot_avg_order, plot_coeff_vs_density, \
    plot_avg_coeff_per_density


def test_one():
    static_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt"
    dynamic_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt"
    n = 0.3
    N = 300
    L= 5
    export_to_ovito(static_file, dynamic_file,L,'results/results_ovito_5_03.dump')
    simulation_result = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/simulation_5_03.txt"
    coeff_df = pd.read_csv(simulation_result, sep=" ", names=["it", "coeff"])
    noises = [0.3]
    coeff_list = [coeff_df]
    plot_order_coeff(coeff_list,noises,L,N)



def test_many():
    static_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt"
    dynamic_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt"

    n = 1
    N = 300
    # export_to_ovito(static_file, dynamic_file,L)

    noises_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/noises.txt"
    noises = pd.read_csv(noises_file, names=["n"])
    l_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/ls.txt"
    l_s = pd.read_csv(l_file, names=["l"])
    coeff_df_list = []

    for j,l in enumerate(l_s.values):
        coeff_df_list.append([])
        # for i in range(len(noises.values)):
        coeff_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/L{l[0]}_coeff_12.txt'
        coeff_df = pd.read_csv(coeff_file, sep=" ", names=["it", "coeff"])
        coeff_df_list[j].append(coeff_df)

    #averge order for different noises
    iteration = 1000
    average_order = []
    std_dev = []

    reduced_coeff_list = []
    reduced_noises = [0.0, 0.4, 0.6, 1.2, 3, 6]

    for j,l in enumerate(l_s.values):
        reduced_coeff_list.append([])
        average_order.append([])
        std_dev.append([])
        for i,c in enumerate(coeff_df_list[j]):
            average_order[j].append(np.average(c["coeff"][iteration:]))
            std_dev[j].append(np.std(c["coeff"][iteration:]))
            # if round(noises.values[i][0],1) in reduced_noises:
            #     reduced_coeff_list[j].append({"it": c["it"],"coeff":c["coeff"]})

    # plot_avg_order(average_order, std_dev,l_s.values, noises, iteration)

    # for i,l in enumerate(l_s.values):
    #     plot_order_coeff(reduced_coeff_list[i], reduced_noises, l[0], N)

    # for i,l in enumerate(l_s.values):
    #     plot_one_avg_order(average_order[i], std_dev[i],l[0],noises,iteration)


    # itero para cada ruido
    # for k,n in enumerate(reduced_noises):
    data = []
    average_coeff = []
    std_coeff = []
    densities = []
    for i,l in enumerate(l_s.values):
        densities.append(N/(l[0]^2))
        data.append(reduced_coeff_list[i][3]) #ruido 0.4
        average_coeff.append(np.average(reduced_coeff_list[i][3]["coeff"][iteration:]))
        std_coeff.append(np.std(reduced_coeff_list[i][3]["coeff"][iteration:]))

    plot_coeff_vs_density(data, l_s.values, N)
    plot_avg_coeff_per_density(densities, average_coeff,std_coeff, iteration, N)



    #n/(l^2)

if __name__ == '__main__':
    # test_one()
    test_many()
