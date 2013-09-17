Modularizator
=============
Author: Vahan Hovhannisyan

INTRODUTION.
This plugin is intended to improve a Java project's modularization, i.e. to give the user suggestions how to arrange the classes into packages so that a certain Q score is maximized. The algorithm used here is a randomized heuristic authored by Marcelo Zanetti. All visualizations are done with Gephi Toolkit.

PROPERTIES VIEW.
To open the plugin's view in Eclipse go to Window -> Show View -> Other and double click on Modularizator Properties View. This view contains several attributes to customize the algorithms and the plugin itself.
	T - The temporature of the network to be achieved after modularization. The higher it is, that less the network will be modularized. The default value is 5.0E-7
	Number of Steps - The number of steps to run the algorithm. The default value is 100.000
	Number of steps for the layout algorithm - The number of steps for the layout algorithm. Here Yifun Hu's layout algorithm is used for visualizing networks. The default value is 10.000
	Output folder - The folder where all the output files (PNG images and GEXF XML outputs of the network before and after modularization and a .TXT log file). The default value is the current folder.
	Algorithm - Here you can choose which modularization algorithm to run.
	Scorer - Here you can choose which scorer method to use for showing the modularization score

CONTEXT MENU.
To run the algorithm right-click on the project, go the Modularizator menu and choose Modularize. 
The same menu also contains
	Fix All - Fixes all marks by moving the files into according packages. Note that all references to the files and in the class are updated accordingly and this operation CANNOT be undone.
	Visualize - Gives a visualization of the project's network.
	Score - Gives the Q score of the project's network.
	Clear Markers - Clears all markers made by the Modularizator plugin.
	Modulzarize - This will run the modularization algorithm, give output images and graph exports, also it will add markers on files that are suggested to be moved.

ADDING NEW ALGORITHMS/SCORERS (FOR DEVELOPPERS)
To add a new algorithm or a scorer make sure to implement the following steps:
	Create the according class (in package modularizator.logic) extending Algorithm / Scorer
	Add the appropriate type to the AlgorithmTypes / ScorerTypes enum
	Add the appropriate case to the switch structure in Modularizator.initAlgorithm() / initScorer() method

Have fun!

BUGS / TODOS:
1. Now you have to close all other projects before modularizing, otherwise all open onces get into the network. Make sure to check if the compilation unit belongs to the desired project when reading it.
2. Delete the according Marker from the Problems list, only if the file was actually moved
3. Create the according package, if it doesn't exist while initializing a new Cluster
 





