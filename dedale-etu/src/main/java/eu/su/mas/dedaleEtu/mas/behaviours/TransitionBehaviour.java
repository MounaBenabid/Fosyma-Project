package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class TransitionBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2419642717395909667L;
	
	public TransitionBehaviour(final AbstractDedaleAgent myagent) {
		super(myagent);
	}
	
	@Override
	public void action() {
		FSMBehaviour fsmChasse = new FSMBehaviour(this.myAgent);
		/** on établit ici si on est dans une grille ou un arbre en
		testant si des nodes ont 1, ou plus de 4 edges (on est donc
		dans un arbre)
		
		on fait une stratégie de chasse différente pour les deux :
		- pour une grille on veut coincer le golem à une extrémité
		- pour un arbre on veut se positionner sur un noeud racine
		
		first part commune aux deux : on regarde nos alentours et on
		identifie s'il y a stench ou pas - sinon on n'entre pas en
		communication
		
		communication pareille les deux :
		on partage la liste de nodes qui ont une odeur quand on la 
		sent + notre position
		
		chasse behaviour différente :
		partie commune : si les nodes observables ont une odeur, choisir
		parmi eux, sinon si on a une liste de noeuds à odeur, choisir
		le plus proche
		
		si on n'a aucun des deux, choisir un node random :
		- pour un arbre : choisir un noeud parmi les observables qui ne
		sont pas ou ne mènent pas qu'à des feuilles
		- pour une grille : un noeud random parmi les observables sans
		compter le noeud sur lequel on était au pas de temps précédent
		et les noeuds qui ont deux ou trois edges
		
		si on a un des deux :
		- pour un arbre : on suit l'odeur en choisissant un noeud
		random parmi ceux qui ne sont pas une feuille
		
		si on est sur un noeud racine qui a une odeur, on ne bouge pas 
		jusqu'à ce que le noeud n'ait plus d'odeur - on recommence 
		à suivre l'odeur
		
		on compte le nombre de fois où on n'a pas bougé de place
		si le compteur atteint 5, on envoie un signal chaque pas de
		temps comme quoi le golem est bloqué, les autres agents peuvent
		partir
		l'agent se déconnecte une fois qu'il a reçu un accusé de
		réception de tous les agents
		
		COMMENT DETERMINER QU'UN NOEUD EST RACINE ::
		on est sur un node à odeur, et un des nodes environs qui ont
		une odeur est une feuille (il n'a qu'un edge)
		OU un des nodes environs a deux edges et le node de son deuxième
		edge est une feuille
		
		on ne se déplace jamais sur un node qui a une odeur si c'est
		une feuille
		
		- pour une grille : on suit l'odeur en choisissant un noeud
		random parmi ceux qui ont une odeur : si plusieurs noeuds ont
		une odeur, choisir parmi ceux qui n'ont pas deux edges
		
		si on est sur un noeud qui a une odeur et dont un des nodes 
		adjacents a une odeur et a deux edges, ne pas bouger tant que 
		notre node a une odeur et communiquer aux agents la position
		présumée du golem en plus de la nôtre, pour que l'agent se place
		sur l'autre node lié à celui du golem (s'il a une odeur, sinon
		continuer de suivre l'odeur)
		 * 
		 */
	}

}
