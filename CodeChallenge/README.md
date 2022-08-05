Input: 

a.tryDirectDependency("A B C");
a.tryDirectDependency("B C E");
a.tryDirectDependency("C G");
a.tryDirectDependency("D A F");
a.tryDirectDependency("E F");
a.tryDirectDependency("F H");

        // CyclicDependencyException example
        aa.tryDirectDependency("A B");
        aa.tryDirectDependency("B A");


Output:

A depends transitively on B,C,E,F,G,H
B depends transitively on C,E,F,G,H
C depends transitively on G
D depends transitively on A,B,C,E,F,G,H
E depends transitively on F,H
F depends transitively on H
G depends transitively on 
H depends transitively on 
