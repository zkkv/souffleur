# Souffleur

Souffleur is an IntelliJ IDEA plugin that provides inline code suggestions using large language models.

## Installation

### Prerequisites
Right now, the only LLMs supported are LLama and Phi that should be run locally. To use the plugin, you will need a Docker container running with the model in the background.

The command below will create a new container named `ollama` from an image named `ollama/ollama`. If you don't have an image with that name, it will first pull it from a repository.
```shell
docker create -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
```

Now start the container:
```shell
docker start ollama
```

Choose a `<model>` you would like to use from the list below:
```
phi3.5
llama3.2:1b
```

The container on its own only has an Ollama CLI but not the model yet. To get the necessary model, execute:
```shell
docker exec -it ollama ollama pull <model>
```

You are ready to use the plugin. If you want to stop the container, run:
```shell
docker stop ollama
```


### Building
1. Start by cloning the repository and navigating to the root folder.
2. Open the terminal.
3. Run one of the following.
- Mac/Linux:
```shell
./gradlew build
```
- Windows:
```shell
gradlew.bat build
```
Alternatively, use an IDE in which you can execute Gradle `build` configuration.

### Choosing Model
If you pulled `phi3.5` in the Docker, you don't have to change anything. If you pulled any other model, you will need to go to `souffleur.kt` and replace the model constructor with the respective model. For example, instead of `Phi` class you need to use `Ollama`. 

### Running
1. Similarly to `build`, execute `runIde` (it might be called  `Run Plugin`). IntelliJ window will appear.
2. Select any project or create a new one.
3. Try typing a character and wait for a grey inline suggestion to appear.

## Tests
Unit tests running with JUnit 5 don't test the plugin but only the inner classes. You can run them with gradle `test` configuration. Plugin tests use JUnit 3 and can be executed with `testPlugin` configuration. All tests can be run with `testAll` configuration.

Code coverage report can be generated with `koverHtmlReport` configuration (run it after running tests). Find the report under `build/reports/kover/html/index.html`.

## Design
The plugin class inherits from `InlineCompletionProvider` and uses an instance of `LanguageModel` under the hood (currently `Phi`). That model is queried and then returns a suggestion. More models can be easily added in the future because they follow a very simple interface.

Ollama uses a cache based on a Trie data structure that stores code prefixes. If the prefix is found in the cache, it uses the suggestion associated with that prefixes instead of querying the LLM again.

## Project Structure
```
src
├── main
│   ├── kotlin/com/github/zkkv/souffleur/
│   │   ├── helpers/      # Utility methods
│   │   ├── interfaces/   # Interfaces for LLMs and data structures
│   │   ├── models/       # LLMs
│   │   ├── structures/   # Data structures
│   │   └── souffleur.kt  # Entry point of the plugin
│   └── resources  # Plugin-related files
└── test # Unit tests organized the same way as source files
```

## Screenshots

![Screenshot of the IDE where `return` is completed by `a * b;` within a function named `product`](./assets/img/completion_example.png)

## License
This plugin is licensed under [GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/). All dependencies are licensed under their respective licenses.

## Developer
Developed by zkkv, 2024
