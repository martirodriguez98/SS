import pandas as pd
from typing import Dict

import plotly.graph_objects as go

def plot_order_coeff(coeff_df_list: list, noises, L, N):
    # it = coeff_df["it"]
    #
    # data = coeff_df["coeff"]
    data = []

    for i in range(len(noises)):
        data.append(go.Scatter(
            x = coeff_df_list[i]["it"],
            y = coeff_df_list[i]["coeff"],
            name=noises[i],
            mode='lines',
        ))

    fig = go.Figure(
        data=data,
        layout = go.Layout(
            title=dict(text=f'Order coefficients per iteration for: {N} particles - L: {L}'),
            xaxis=dict(title='Iteration'),
            yaxis=dict(title='Order coefficient', rangemode='tozero'),
            legend=dict(title='Noises')
        )
    )
    fig.show()

def plot_avg_order(average_order, std_dev,l_s, noises, iterations):
    data = []
    for i in range(len(l_s)):
        data.append(go.Scatter(
            x = noises["n"],
            y = average_order[i],
            mode='lines',
            name=str(l_s[i][0])
        ))
    fig = go.Figure(
        data = data,
        layout=go.Layout(
            title=dict(text=f'Average order for different noises from iteration {iterations}'),
            xaxis=dict(title='noise'),
            yaxis=dict(title='Average order coefficient'),
            legend=dict(title='L')
        )
    )
    fig.show()

def plot_one_avg_order(average_order, std_dev, l, noises, iterations):
    fig = go.Figure(
        data = go.Scatter(
            x=noises["n"],
            y =average_order,
            mode='markers',
            error_y=dict(array=std_dev),
        ),
        layout=go.Layout(
            title=dict(text=f'Average order for different noises from iteration {iterations} for L = {l}'),
            xaxis=dict(title='noise'),
            yaxis=dict(title='Average order coefficient'),
        )
    )
    fig.show()

#variation for same noise with different Ls
