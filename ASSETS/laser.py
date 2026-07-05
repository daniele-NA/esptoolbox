import os
import sys
from PIL import Image

# python ASSETS/laser.py /Users/<user>/Downloads/<source>


def crop_bottom(src_folder, height):
    dst = os.path.join(src_folder, "edited")
    os.makedirs(dst, exist_ok=True)

    for name in os.listdir(src_folder):
        path = os.path.join(src_folder, name)
        if not name.lower().endswith((".jpg", ".jpeg", ".png")):
            continue

        img = Image.open(path)
        w, h = img.size

        cropped = img.crop((0, 0, w, height))

        cropped.save(os.path.join(dst, name))


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python laser.py <path_cartella> [altezza]")
        sys.exit(1)

    folder_path = sys.argv[1]
    height = 1900
    if len(sys.argv) > 2:
        try:
            height = int(sys.argv[2])
        except ValueError:
            print("Errore: l'altezza deve essere un numero intero.")
            sys.exit(1)

    crop_bottom(folder_path, height)


