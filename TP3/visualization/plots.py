from pandas import DataFrame
import plotly.graph_objects as go
import numpy as np


def plot_avg_collision_time(times_ns, n_values):
    data = []

    for i, times in enumerate(times_ns):
        avg_collision_freq = len(times) / times[-1]

        std_dev = np.std(times)
        counts, bin_edges = np.histogram(times, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:]) / 2

        data.append(go.Scatter(
            x=bin_centres,
            y=counts,
            mode='lines',
            name=f'N={n_values[i]}'
        ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'Colisiones', x=0.5),
            xaxis=dict(title='Tiempo de colisión (s)'),
            yaxis=dict(title='PDF'),
        )
    )
    fig.update_layout(width=1000, height=1000)
    fig.show()


def plot_small_particles_speed_distribution(speeds_ns, n_values):
    for i, speeds in enumerate(speeds_ns):
        data = []
        initial_speed = speeds[0]
        counts, bin_edges = np.histogram(initial_speed, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:]) / 2

        data.append(go.Scatter(
            x=bin_centres,
            mode="lines+markers",
            y=counts,
            name=f'Valores iniciales N={n_values[i]}'
        ))

        last_third = speeds[-int(len(speeds) / 3):]
        counts, bin_edges = np.histogram(last_third, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:]) / 2.

        data.append(go.Scatter(
            x=bin_centres,
            mode="lines+markers",
            y=counts,
            name='Último tercio'
        ))

        fig = go.Figure(
            data=data,
            layout=go.Layout(
                title=dict(text=f'Speed N={n_values[i]}'),
                xaxis=dict(title=f'Rapidez (m/s)'),
                yaxis=dict(title="PDF")
            )
        )
        fig.update_layout(width=1000, height=1000)
        fig.show()


def plot_big_particle_route(big_particle_pos, radio, L):
    speeds = ["v=[0,2] m/s", "v=[2,3] m/s", "v=[3,4] m/s"]
    colors = ["#e69cb2", "#a0db8e", "#85a8e0"]
    fig = go.Figure(
        layout=go.Layout(
            xaxis=dict(title="X (m)",range=[0,L]),
            yaxis=dict(title="Y (m)",range=[0,L]),
            legend=dict(
                yanchor="top",
                y=0.99,
                xanchor="right",
                x=0.99
            ),


        ),

    )
    for i, pos in enumerate(big_particle_pos):
        size = len(big_particle_pos[i]) - 1
        xs = []
        ys = []
        for pos in big_particle_pos[i]:
            xs.append(pos[0])
            ys.append(pos[1])

        fig.add_trace(go.Scatter(
            x=xs,
            y=ys,
            name=speeds[i],
            mode="lines",
            line=dict(color=colors[i])
        ))

        x = big_particle_pos[i][size][0]
        y = big_particle_pos[i][size][1]
        x0 = x - radio
        x1 = x + radio
        y0 = y - radio
        y1 = y + radio
        fig.add_shape(
            type="circle",
            xref="x", yref="y",
            x0=x0, y0=y0, x1=x1, y1=y1,
            fillcolor=colors[i],
            opacity=0.3,
            line=dict(width=0)
        )

    fig.update_layout()
    fig.update_layout(width=1000, height=1000)
    fig.show()


def plot_big_particle_coeff_distribution(data, runs):
    time_step = 0.1
    clock_dfs = []

    for frames in data:
        current_time = 0
        clock_df = []
        for info in frames:
            if current_time <= info[0]:
                clock_df.append(info)
                current_time += time_step
        clock_dfs.append(clock_df)

    min_clock_dfs = min(clock_dfs, key=lambda c_df: c_df[-1][0])
    min_len = len(min_clock_dfs)
    min_time = min_clock_dfs[-1][0]
    particle_initial_pos = [[data[0][1],data[0][2]]]

    difussion_coeffs = []
    for df in clock_dfs:
        big_p_positions = []
        for info in df:
            big_p_positions.append([[data[0][1],data[0][2]]])
            difussion_coeffs.append(np.linalg.norm(big_p_positions[:min_len] - big_p_positions, axis=1))

    DCM = np.mean(difussion_coeffs, axis=0)
    x_values = np.arange(0, min_time, time_step)
    error = np.std(difussion_coeffs, axis=0)
    start_time = 2
    start_index = int(start_time/time_step)




def plot_small_particles_coeff_distribution():
    pass
