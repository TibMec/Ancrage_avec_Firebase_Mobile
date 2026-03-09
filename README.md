# Ancrage Enseignant - Conseiller Pédagogique, version Mobile (Android)
 __!!__ ***Code construit entierement à l'aide de ChatGPT*** __!!__

 ## ARCHITECTURE
 
 ![IMG_2113](https://github.com/user-attachments/assets/dba02ac8-7e68-4f35-8373-4a78c839762d)

 ### EXPLICATIONS
 - Le MAIN a 2 boutons:
   - Se connecter -> Renvoie à l'activité LOGIN
   - Créer un compte -> Renvoie à l'activité REGISTER  
      <img width="242" height="456" alt="image" src="https://github.com/user-attachments/assets/8df8abe0-6cc3-40c7-ac67-783064bfbdeb" />  


- L'Activité LOGIN:  
  Renvoie à la page profil désignée en fonction du courriel:
  - Si l'utilisateur s'est inscrit comme prof, il ira automatiquement vers un profil prof
  - S'il s'est inscrit comme conseiller, il ira dans le profil conseiller  
      <img width="242" height="460" alt="image" src="https://github.com/user-attachments/assets/35ba27ce-24ce-4da3-887e-bd4a332da030" />  

- L'activité REGISTER:  
  Contient tous les champs nécessaires à la création de deux comptes, en fonction du bouton radio ``Role``
  - Si Prof est sélectionné, il n 'y a pas de champ ``tel``  
      <img width="240" height="452" alt="image" src="https://github.com/user-attachments/assets/2f2b553e-db41-48a9-9d2b-b15da451cec8" />  

  - Si Conseiller est selectionné, ``tel`` apparait et le champ ``écoles`` disparait  
      <img width="238" height="460" alt="image" src="https://github.com/user-attachments/assets/ca4d6d8a-f03d-428f-ab16-bfaa0665749b" />  

  - Les champs ``écoles`` et ``codes matières`` renvoient un modal à sélection multiple  
      <img width="233" height="461" alt="image" src="https://github.com/user-attachments/assets/2c76958c-919c-4141-9f5a-b3e94ff130da" />  
      
  - Le remplissage adéquat ramène au MAIN après validation  
      <img width="225" height="457" alt="image" src="https://github.com/user-attachments/assets/f1719446-778d-4e2c-a749-fa88f9d697cb" />  


- L'Activité PROF:
  - A deux boutons CRUD, ``update`` et ``delete``, non fonctionnels.
  - A un bouton renvoyant à une activité affichant la liste des conseillers pédagogiques qui lui sont affiliés  
      <img width="230" height="455" alt="image" src="https://github.com/user-attachments/assets/cfa0ac31-7df0-4cbf-b543-ae27c5ff6060" />  

- L'activité CONSEILLER:
  - A les memes boutons CRUD non fonctionnels
  - A un bouton renvoyant à une activité listant tous les profs inscrits  
      <img width="235" height="458" alt="image" src="https://github.com/user-attachments/assets/02b8d809-8da3-4a3b-817b-e52026958657" />  

- Activité Liste CONSEILLERS:  
      <img width="230" height="452" alt="image" src="https://github.com/user-attachments/assets/527fa2fd-fa22-4ad0-b7ff-a08006a89a4d" />  
  - Le bouton téléphone est cliquable pour appeler directement  
      <img width="221" height="457" alt="image" src="https://github.com/user-attachments/assets/1e701002-8bd1-4601-90f5-ebd593ede264" />  

- Activité Liste PROFS:  
      <img width="238" height="460" alt="image" src="https://github.com/user-attachments/assets/a3b410d4-60ec-46a9-9160-f7dd3691c8df" />  




Les retours sont effectués à l'aide de la touche retour triangle (fait sur Emulateur PIXEL 5 pour raisons techniques)  

---

## DATA
La DB est conçue en json via Firebase, dans une Realtime database.
Les deux collections Profs et Conseillers sont ici deux objets json cohabitant dans un même objet parent (la Realtime DB ne permet qu'une seule collection).
L'authentification est également gérée par Firebase.  
<img width="543" height="425" alt="image" src="https://github.com/user-attachments/assets/a8698049-ea98-4b53-9abd-67127465f401" />  

<img width="767" height="389" alt="image" src="https://github.com/user-attachments/assets/cdee3d3b-f2f4-4d4d-acbd-90af31987cc9" />  

---

## SPECIFICATIONS
- Gradle version 9.1.0
- Compile SDK: API 36.0 ("Baklava"; ANdroid 16.0)
- Java 11

---

## CONCLUSION
- Pas de liens aux services nécessaires construits auparavant en python, gestion CRUD par Firebase pour plus de praticité
- Idem pour l'authentification
- Pas de gestion des Locales, seulement français disponible



    
