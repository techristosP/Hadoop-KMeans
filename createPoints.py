import os
import random, math, matplotlib.pyplot as plt

def create_points(centers, num_points):
    n_cluster1 = (0, num_points // 3)
    n_cluster2 = (n_cluster1[1], num_points // 3 * 2)
    n_cluster3 = (n_cluster2[1], num_points // 3 * 3)
    n_cluster = [n_cluster1, n_cluster2, n_cluster3]
    # print(len(range(n_cluster1[0], n_cluster1[1]))==len(range(n_cluster2[0], n_cluster2[1]))==len(range(n_cluster3[0], n_cluster3[1])))
    # print(n_cluster1, n_cluster2, n_cluster3)
    points = []
    
    for n_cl in n_cluster:
        for i in range(n_cl[0], n_cl[1]):
            center = centers[0]
            distance = random.expovariate(0.05)
            angle = random.uniform(0, 2 * math.pi)

            x = center[0] + distance * math.cos(angle)
            y = center[1] + distance * math.sin(angle)

            points.append((x, y))

        centers = centers[1:]

    return points

def write_to_file(points, filename):
    path = os.path.expanduser(f'~/Desktop/{filename}')
    with open(path, 'w') as f:
        for point in points:
            f.write(f'{point[0]} {point[1]}\n')

if __name__ == '__main__':
    centers = [(-100, 200), (300, 300), (0, -100)]
    num_points = 10**6+3

    points = create_points(centers, num_points)
    write_to_file(points, 'data.txt')

    print(points[:10])
    plt.scatter(*zip(*points))
    plt.plot(*zip(*centers), 'ro')
    plt.show()




        