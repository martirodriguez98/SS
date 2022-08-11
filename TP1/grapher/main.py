import sys

from grapher.parser import parse_particles, add_neighbours
from grapher.plot import plot

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
    for i in range(len(particles)):
        plot(particles,i+1)


