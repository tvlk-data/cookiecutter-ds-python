def main(params={}, env_vars={}, meerkat=None, **kwargs):
    print("Start training")
    print("Hyperparameters", params)            # This params are injected from rm.yaml
    print("Environment variables", env_vars)    # This env_vars are injected from rm.yaml

    # To get parameter value
    # learning_rate = params.get('learning_rate', 1e-4)

    # To log parameter
    # meerkat.log_param("learning_rate", learning_rate)

    # To log metric
    # meerkat.log_metric("accuracy", accuracy)

    # To save trained model to GCS
    # meerkat.upload_model_directory(saved_model_dir_path)