from grapher.position import Position


class Particle:
    neighbours: list = []
    position: Position

    def __init__(self, id, radio):
        self.id = id
        self.radio = radio

    def set_position(self, position):
        self.position = position

    def set_neighbours(self, neighbours):
        self.neighbours = neighbours

    def __repr__(self) -> str:
        return "id: " + str(self.id) + " pos: " + str(self.position) + " r: " + self.radio

    def print_neighbours(self):
        n = []
        for p in self.neighbours:
            n.append(p.id)
        print(n)