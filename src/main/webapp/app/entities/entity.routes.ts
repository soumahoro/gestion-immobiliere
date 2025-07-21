import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gestionimmobiliereApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'administrateur',
    data: { pageTitle: 'gestionimmobiliereApp.administrateur.home.title' },
    loadChildren: () => import('./administrateur/administrateur.routes'),
  },
  {
    path: 'annee',
    data: { pageTitle: 'gestionimmobiliereApp.annee.home.title' },
    loadChildren: () => import('./annee/annee.routes'),
  },
  {
    path: 'appartement',
    data: { pageTitle: 'gestionimmobiliereApp.appartement.home.title' },
    loadChildren: () => import('./appartement/appartement.routes'),
  },
  {
    path: 'locataire',
    data: { pageTitle: 'gestionimmobiliereApp.locataire.home.title' },
    loadChildren: () => import('./locataire/locataire.routes'),
  },
  {
    path: 'moi',
    data: { pageTitle: 'gestionimmobiliereApp.moi.home.title' },
    loadChildren: () => import('./moi/moi.routes'),
  },
  {
    path: 'proprietaire',
    data: { pageTitle: 'gestionimmobiliereApp.proprietaire.home.title' },
    loadChildren: () => import('./proprietaire/proprietaire.routes'),
  },
  {
    path: 'reglement',
    data: { pageTitle: 'gestionimmobiliereApp.reglement.home.title' },
    loadChildren: () => import('./reglement/reglement.routes'),
  },
  {
    path: 'residence',
    data: { pageTitle: 'gestionimmobiliereApp.residence.home.title' },
    loadChildren: () => import('./residence/residence.routes'),
  },
  {
    path: 'utilisateur',
    data: { pageTitle: 'gestionimmobiliereApp.utilisateur.home.title' },
    loadChildren: () => import('./utilisateur/utilisateur.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
