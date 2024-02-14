<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->

<a name="readme-top"></a>

<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">

<h1 align="center">PCL</h1>

  <p align="center">
    canAda compiler
    <br />
    <a href="https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u/-/tree/main/docs"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://gitlab.telecomnancy.univ-lorraine.fr/pcd2k24/codingweek-12/-/commits/master"><img alt="pipeline status" src="https://gitlab.telecomnancy.univ-lorraine.fr/pcd2k24/codingweek-12/badges/master/pipeline.svg" /></a>
    <br />
    <a href="https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u">View Demo</a>
    ·
    <a href="https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u/-/issues">Report Bug</a>
    ·
    <a href="https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u/-/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

PCL is a project developed in Java by a team of engineering students. It is a compiler designed specifically for the "canAda" language, which is based on Ada but with simplified features. This project aims to provide a reliable and efficient tool for compiling and executing canAda programming code.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

- ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
- ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
- ![GitLab CI](https://img.shields.io/badge/gitlab%20ci-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=white)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

To get a local copy up and running follow these simple steps.

### Installation

1. Clone the repo
   ```sh
   git clone https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u.git
   ```
2. Build all
   ```sh
   cd mezureux1u
   ./gradlew assemble
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

### Parse a file

```sh
USAGE: ./gradlew run --args="[OPTIONS] FILE"
  INPUT           : Input file
  OPTIONS:
     -a (--ast)   : Generate the .png AST (default: false)
     -h (--help)  : Print this message (default: true)
     -p (--parse) : Parse the input file (default: false)
```

**Examples:**

```sh
./gradlew run --args="-p tests/src/unDebut.ad -a"
```

## Roadmap

- [X] LEXER

- [X] PARSER
  - [x] Display AST with Graphviz

See the [open issues](https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u/-/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

- Yann DIONISIO <<yann.dionisio@telecomnancy.eu>>
- Adrien LAROUSSE <<adrien.larousse@telecomnancy.eu>>
- Mathis MANGOLD <<mathis.mangold@telecomnancy.eu>>
- Stanislas MEZUREUX <<stanislas.mezureux@telecomnancy.eu>>

Project Link: [https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u](https://gitlab.telecomnancy.univ-lorraine.fr/Stanislas.Mezureux/mezureux1u)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

- [QuickChart GraphViz API](https://quickchart.io/documentation/graphviz-api/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
