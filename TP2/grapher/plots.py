import pandas as pd
from typing import Dict

import plotly.graph_objects as go

def plot_order_coeff(coeff_df_list: list, noises: pd.DataFrame, L, N):
    # it = coeff_df["it"]
    #
    # data = coeff_df["coeff"]
    data = []

    for i in range(len(noises)):
        data.append(go.Scatter(
            x = coeff_df_list[i]["it"],
            y = coeff_df_list[i]["coeff"],
            name=noises.values[i][0],
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

# def plot_order_vs_noise():