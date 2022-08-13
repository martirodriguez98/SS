from typing import List

import numpy as np
import matplotlib.pyplot as plt


def plot(particles, id):
    rc = 6
    plt.style.use('dark_background')
    nb = particles[id - 1].neighbours
    fig = plt.figure()
    ax = fig.add_subplot()

    for p in particles:
        if p.id == id:
            ax.scatter(float(p.position.x) , float(p.position.y) ,s=4 * pow(float(p.radio),2), color='#ff1493')
            ax.annotate( p.id, (float(p.position.x) , float(p.position.y)))
            circle = plt.Circle((float(p.position.x) , float(p.position.y)), rc + float(p.radio), color='pink', fill=False, linewidth=1)
            ax.add_patch(circle)
        elif p in nb:
            ax.scatter(float(p.position.x) , float(p.position.y), s=4 * pow(float(p.radio),2), color='#91db57')
        else:
            ax.scatter(float(p.position.x), float(p.position.y), s=4 * pow(float(p.radio),2), color='white')

    plt.axis("equal")
    ax.set_aspect('equal', adjustable='box')

    # plt.xlim(0,100)
    # plt.ylim(0,100)


    plt.show()
    # plt.savefig(f'plots/{id}.png')

def plot_time(time, mode):
    plt.style.use('dark_background')
    fig = plt.figure()
    ax = fig.add_subplot()
    new_time = []
    for t in time:
        new_time.append(float(t.replace('\n','')))

    n_list = list(reversed(list(range(1,len(time)+1))))

    new_list = zip(*sorted(zip(n_list, new_time)))
    plt.plot(*new_list)
    plt.xlabel("Amount of particles (N)")
    plt.ylabel("Time (miliseconds)")
    plt.title("Relation execution time / amount of particles - " + mode)
    plt.show()