
import plotly.graph_objects as go


def plot(all_states, title, legends):
    data = []
    for i in range(len(all_states)):
        x = []
        y = []
        for row in all_states[i].iterrows():
            x.append(row[1]['t'])
            y.append(row[1]['x'])
        data.append(go.Scatter(
            x = x,
            y = y,
            name = f'{legends[i]}',
            mode='lines'
        )),

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            title=dict(text=f'{title}'),
            xaxis=dict(title="Time"),
            yaxis=dict(title="Position")
        ),
    )
    fig.update_layout(width=1000, height=1000)

    fig.show()