# Cookiecutter Data Science for Python

*Standardized and flexible project structure for doing and sharing data science work.*

This cookiecutter built as a vision to make easier for people doing and sharing 
data science with readability and reproducibility grade. When your teams/organization
have similar convention on how to define project structure, it'll be lot easier for
people to learn and replicate your code faster. This structure inspired from the popular 
[data science cookiecutter](https://drivendata.github.io/cookiecutter-data-science/).

This repo is extracted from [cookiecutter-templates repo](https://github.com/tvlk-data/cookiecutter-templates), and based on TVLK Data's research: [Reproducible Data Science - Repository Structure
](https://docs.google.com/document/d/11Qn6ZZdlLh-_4wB5yzMr0VdXWfYVi3BG0pKHGzTEf_s/edit#heading=h.n3j3cqhdhgja) <credit to @hadyan-tvlk>

## Resulting Directory Structure
The structure of project will be look like this
```
.
├── AUTHORS
├── bin                 <- Binary script used for various tasks
│   ├── evaluate.sh
│   └── train.sh
├── data                <- Contain dataset used for experiment
│   ├── external        <- Data coming from external resources
│   ├── interim         <- Intermediate data yielded from specific transformation (not ready to use)
│   ├── processed       <- Final, canonical data used for training and testing
│   └── raw             <- Original data (Should not be modified)
├── Dockerfile          <- Dockerfile used for experiment
├── docs                <- Full documentation of your experiment. Can be as simple as list of markdown or using existing tool (e.g: Sphinx)
│   └── overview.md
├── Makefile            <- Act as your data science pipeline
├── notebooks           <- Where you put all your Jupyter notebook files
│   ├── 01-Data Exploration-deff.ipynb
│   ├── 02-Data Preprocessing-deff.ipynb
│   └── 03-Training Model-deff.ipynb
├── outputs             <- Where you save all the generated files (e.g.: model, chart, graph)
│   ├── figures
│   └── models
├── README.md
├── reports             <- Where you put all the experiment results report and analysis
├── requirements.txt    <- Software requirement
├── setup.py
├── src
│   ├── __init__.py
│   ├── datasets
│   │   ├── __init__.py
│   │   ├── base.py
│   ├── models
│   │   ├── __init__.py
│   │   ├── base.py
│   ├── utils
│   │   └── __init__.py
│   └── visualization
│       └── __init__.py
└── tests
    └── __init__.py
```

# Notes
- Above structure describe common definition used in data science project. There might some parts that might not relevant (not used) or still missing. We're trying to keep this as flexible as possible.
- When using Jupyter notebooks, it's preferred to separate between exploratory notebook and final one. `Exploratory Notebook` intended only for your experiment purpose while the `Final Notebook` should be used for final form of your experiment that used to communicate with other people.

  Besides, try naming the file with meaningful name. One of the recommended naming:
  
  ``<STEP_OF_EXPERIMENT>-<NAME OF THE EXPERIMENT>-<EXPERIMENTER_NAME>.ipynb``. For example:
  
  ``01-Data Exploration-Deff.ipynb``.
  
  The way we need to put name of the people who do the experiment to prevent the mix-up when other people come up and try to make collaboration to the notebook. Instead, they will have their own notebook file.
  
- The reason we need to define data science pipeline is to enable for other people can run your project without really need knowing the details of how it's done. However, it's little bit tricky since we don't really have specific tools (common one) to do that. After done some research, there are several alternative options to enable this:
  - **Makefile**
  
    This preference come with simplicity (The syntax is simple) and portability (mainly used in UNIX-based platforms, but also available for Windows). The main feature is `Make` able to recording what you're already doing with its core concept of generated files depend on other files. 
    
    The only trade-off maybe come from the syntax form that not easy to read. This might confusing for new people.
    
    Besides that trade-off, you should try to use Makefile as your pipeline tools, since it's simple enough to use (even [Data Veteran recommend this](https://bost.ocks.org/mike/make/)).
  - **CMAKE**
    
    Stands for Cross-platform Make (you might need this when you want to ensure all projects run in all platforms). This tool provide more advance features than Makefile, but in result for more complex usage. This tool will do two things: 1) generate the Make file, different format for different system, 2) Do the actual build.
    
    Not sure if this overkill for our usage (unless you're creating complex project that required heavy builds). For more reading, please visit [here](https://prateekvjoshi.com/2014/02/01/cmake-vs-make/).
    
  - **Data Version Control (DVC)**
    
    Still in early stage, this tool trying to solve pipelining problem by build tools that act like git to store our DS pipeline (as metadata file) inside our project, so other people can do the reproducible by use the metadata. This might worth to try since designed as first-class citizen for data science. Read more in [here](https://dvc.org/).

# Contributing
This Cookicutter still on active development, we'd love to hear what works for 
you and what doesn't, Pull Requests and Submit an Issues are welcomed.
