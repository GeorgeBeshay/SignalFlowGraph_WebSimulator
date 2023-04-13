# Signal Flow Graph Web Simulator
`GUI for generating and solving SFGs` <br>
`Stability test using Routh Hurwitz criteria`

---

* We’ve been asked to design and implement a GUI application that supports the drawing of signal flow graphs with all of its components (nodes, branches, gains).
* The application should be able to calculate and find all of the following from the graphical representation of the SFG:
    * All the forward paths
    * All the individual loops
    * All combinations of n-non touching loops.
    * All values of Δ, Δi - where i is represents the forward path number -
    * The overall transfer function.
* The application should provide the user with the capability of inserting the characteristic equation of the system and using it, the following should be calculated:
    * Declaring whether the system - whose characteristic equation had been inserted - is stable or not.
    * If the system being checked is not stable, the number of poles that do exist in the RHS of the s-plane should be calculated.
    
---


## Table of Contents

- [Introduction](#introduction)
- [Installation](#installation)
- [Designed & Implemented By](#designed-&-implemented-by)

---

## Introduction

* This lab was developed as a part of the academic course ***CSE 241: Control Systems*** that is being studied on the 6th semester (spring 2023) in the department of Computer and Systems Engieering, Alexandria University.
*As an application on the SFGs that had been studied in the course, we were required to implement a GUI simulator for it.

---

## Installation

### Steps to install and run this application on your local machine.
* Download the ZIP file from [here](https://github.com/GeorgeBeshay/SignalFlowGraph_WebSimulator/archive/refs/heads/main.zip)
* Extract it
* Run the backend server on your local machine using the java spring boot application that can be found in the ***proj*** directory from you favourite IDE.
* Run the frontend webpage from your terminal using `ng serve` in the ***proj-FE*** directory
* Go to `http://localhost:4200/` from your browser

---

## Designed & Implemented By
- ***[Mariam Aziz](https://github.com/MariamAziz0)***
- ***[David Michael](https://github.com/dave-nagib)***
- ***[Patrick Georges](https://github.com/)***
- ***[Marshelino Maged](https://github.com/marshelino-maged)***
- ***[George Beshay](https://github.com/GeorgeBeshay)***

---
