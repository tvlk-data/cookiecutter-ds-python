import argparse
import yaml
import os

from src.datasets import preprocessing
from src.models import training
from rm_sdk.tracking.entities import ModelRun

def is_preprocessing(mode):
    return mode == "all" or mode == "preprocessing"

def is_training(mode):
    return mode == "all" or mode == "training"

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-m", "--mode", help="[preprocessing | training | all] (default:all)")
    args = parser.parse_args()
    
    env_vars = {}
    with open("rm.yaml", "r") as stream:
        try:
            config = yaml.load(stream)
            for param in config["params"]:
                if param["type"] == "int":
                    parser.add_argument(f"--{param['name']}", default=param["value"], type=int)
                elif param["type"] == "str":
                    parser.add_argument(f"--{param['name']}", default=param["value"], type=str)
                else:
                    parser.add_argument(f"--{param['name']}", default=param["value"], type=float) # Default is float for hyperparams
            
            for env in config["envs"]:
                if env["type"] == "float":
                    env_vars[env["name"]] = float(env["value"])
                elif env["type"] == "int":
                    env_vars[env["name"]] = int(env["value"])
                else:
                    env_vars[env["name"]] = str(env["value"]) # Default is str for env vars

        except yaml.YAMLError as ex:
            print(ex)

    args = parser.parse_args()
    mode = args.mode

    if is_preprocessing(mode):
        preprocessing.main()

    if is_training(mode):
        run_id = os.getenv('RUN_ID')

        if not run_id:
            raise AssertionError("run_id should be int not none")

        with ModelRun(run_id) as tracker:
            training.main(
                params=vars(args),
                env_vars=env_vars,
                tracker=tracker
            )
