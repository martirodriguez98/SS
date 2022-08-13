import sys

from grapher.parser import parse_particles, add_neighbours, parse_time
from grapher.plot import plot, plot_time

if __name__ == '__main__':
    argv = sys.argv
    if len(argv) > 3:
        results_file = argv[1]
        static_file = argv[2]
        dynamic_file = argv[3]
    else:
        raise ValueError(f'Please provide all files.')

    particles = parse_particles(static_file, dynamic_file)

    add_neighbours(particles, results_file)

    #particles plots

    # for i in range(len(particles)):
    #     plot(particles,i+1)

    for i in range(1,10):
        plot(particles,i)

    plot(particles, 4)
    # plot(particles, 739)
    # plot(particles, 936)

    #time plots
    # times_file = "../algorithm/src/main/resources/Results/times.txt"
    # timesBF_file = "../algorithm/src/main/resources/Results/timesBF.txt"
    # time = parse_time(times_file)
    # timeBF = parse_time(timesBF_file)
    # plot_time(time, "CellIndexMethod")
    # plot_time(timeBF, "Brute force")




