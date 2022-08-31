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
            title=dict(text=f'Coeficientes de orden por iteración para: {N} partículas - L: {L}'),
            xaxis=dict(title='Iteración'),
            yaxis=dict(title='Coeficiente de orden', rangemode='tozero'),
            legend=dict(title='Ruidos')
        )
    )
    fig.update_layout(width=1000, height=1000)
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
            title=dict(text=f'Promedio de orden para diferentes ruidos a partir de la iteración {iterations}'),
            xaxis=dict(title='Ruido'),
            yaxis=dict(title='Promedio de orden'),
            legend=dict(title='L')
        )
    )
    fig.update_layout(width=1000, height=1000)
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
            title=dict(text=f'Promedio de orden para diferentes ruidos a partir de la iteración {iterations} para L = {l}'),
            xaxis=dict(title='Ruido'),
            yaxis=dict(title='Promedio de orden'),
        )
    )
    fig.update_layout(width=1000, height=1000)
    fig.show()

#variation of order coefficient for same noise with different density
def plot_coeff_vs_density(coeff_lists, l_values, N):
    data = []
    for i in range(len(coeff_lists)):
        print(l_values[i][0])
        density = str(round(N / (l_values[i][0] ^ 2),3))
        data.append(go.Scatter(
            x = coeff_lists[i]["it"],
            y = coeff_lists[i]["coeff"],
            name=f'Densidad = {density}, L = {l_values[i][0]}',
            mode='lines'
        )),

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'Coeficientes de orden vs densidad'),
            xaxis=dict(title='Iteración'),
            yaxis=dict(title='Coeficiente de orden'),
            legend=dict(title='Densidad')
        )
    )
    fig.update_layout(width=1000, height=1000)
    fig.show()

def plot_avg_coeff_per_density(densities,averages,std_errors, iterations, N):
    fig = go.Figure(
        data = go.Scatter(
            x=densities,
            y =averages,
            mode='lines',
            error_y=dict(array=std_errors),
        ),
        layout=go.Layout(
            title=dict(text=f'Promedio de orden para diferentes densidades a partir de la iteración {iterations} para N = {N}'),
            xaxis=dict(title='Densidad'),
            yaxis=dict(title='Promedio de orden'),
        )
    )
    fig.update_layout(width=1000, height=1000)
    fig.show()