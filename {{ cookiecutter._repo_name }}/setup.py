from setuptools import find_packages, setup

setup(
    name='{{ cookiecutter._repo_name }}',
    packages=find_packages(),
    version='0.0.1',
    description='{{ cookiecutter.project_desc }}',
    author='Traveloka',
    license='BSD'
)