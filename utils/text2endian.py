# take a string as input
s = input("Enter a string: ")

# convert the string to bytes
b = bytes(s, 'utf-8')

# convert the bytes to hexadecimal
h = b.hex()
h = h.upper()

# split every 8 characters
h = [h[i:i+8] for i in range(0, len(h), 8)]

# split every 8 to 2 characters
for i in range(len(h)):
    h[i] = [h[i][j:j+2] for j in range(0, len(h[i]), 2)]

# print the hexadecimal string
for i in h:
    print("0x", end='')
    for j in range(len(i)-1, -1, -1):
        print(i[j], end='')
    print(" ", end='')
print()