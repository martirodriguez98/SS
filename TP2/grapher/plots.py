from typing import Dict

import plotly.graph_objects as go

def plot_order_coeff(coeff_df: Dict):
    it = coeff_df["it"]

    data = coeff_df["coeff"]
    fig = go.Figure(
        data=go.Scatter(
            x=list(range(0, len(it))), y=data, mode='lines'
        ),
        layout = go.Layout(
            title=dict(text=f'Prueba iter: {len(it)}'),
            xaxis=dict(title='Iteration'),
            yaxis=dict(title='Order coefficient'),
        )
    )
    fig.show()