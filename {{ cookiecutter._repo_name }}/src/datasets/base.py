# this is only sample format on how to design dataset generator. you might have
# different way to design the generator, the point is to make it more readable
# and reusable, so people can understand the workflow and replicate easily.
import sklearn.datasets

class DatasetA(object):
  """Class to generate dataset"""

  def generate_samples(self, data_dir, tmp_dir):
    """Generate examples."""
    # this is where you define process of data generation like downloading,
    # extracting, and preprocess the data.
    pass
