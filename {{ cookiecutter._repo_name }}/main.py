import argparse
from src.datasets import preprocessing
from src.models import training

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-m", "--mode", help="[preprocessing | training | all] (default:all)")
    args = parser.parse_args()
    
    mode = "all"
    if args.mode:
        mode = args.mode

    if mode == "all":
        preprocessing.main()
        training.main()
    elif mode == "preprocessing":
        preprocessing.main()
    elif mode == "training":
        training.main()
