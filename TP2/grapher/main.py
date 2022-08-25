import sys
import pandas as pd
from grapher.ovito_visualization import export_to_ovito
from grapher.plots import plot_order_coeff

if __name__ == '__main__':
    # argv = sys.argv
    # if len(argv) == 3:
    #     static_file = argv[1]
    #     dynamic_file = argv[2]
    # else:
    #     raise ValueError(f'Please provide file with information.')
    static_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/static300.txt"
    dynamic_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/dynamic300.txt"
    L = 25
    export_to_ovito(static_file, dynamic_file,L)
    coeff_file = "/Users/martirodriguez/Documents/ITBA/SS/SS/SS/TP2/algorithm/src/main/resources/coeffList.txt"
    coeff_df = pd.read_csv(coeff_file, sep=" ", names=["it", "coeff"])
    plot_order_coeff(coeff_df)
