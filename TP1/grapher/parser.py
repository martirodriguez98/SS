import tokenize
from typing import List

from grapher.particle import Particle
from grapher.position import Position


def parse_particles(static_file, dynamic_file):
    try:
        static_info = open(static_file, 'r')
        dynamic_info = open(dynamic_file, 'r')
        static_lines = static_info.readlines()
        dynamic_lines = dynamic_info.readlines()

        particles: List[Particle] = []

        for i, line in enumerate(static_lines):
            tokens = line.split()
            if len(tokens) != 1:
                particles.append(Particle(i - 1, tokens[0]))

        for i, line in enumerate(dynamic_lines):
            tokens = line.split()
            if len(tokens) != 1:
                particles[(i - 1) % len(particles)].position = Position(tokens[0], tokens[1])

        return particles

    except FileNotFoundError:
        raise ValueError(f'Check files path.')


def add_neighbours(particles, results_file):
    try:
        results_info = open(results_file, 'r')
        lines = results_info.readlines()
        for i, line in enumerate(lines):
            tokens = line.split()
            # particles[tokens[0]]
            neighbours = []
            for i in range(len(tokens)):
                if i != 0:
                    t = tokens[i].replace('[', '').replace(',', '').replace(']', '')
                    if t != '':
                        neighbours.append(particles[int(t) - 1])
            particles[int(tokens[0]) - 1].set_neighbours(neighbours)

    except FileNotFoundError:
        raise ValueError(f'File {results_file} not found.')

def parse_time(time_file):
    try:
        time_info = open(time_file, 'r')
        lines = time_info.readlines()
        time = []
        for line in lines:
            time.append(line)
        return time
    except FileNotFoundError:
        raise ValueError(f'File {time_file} not found.')