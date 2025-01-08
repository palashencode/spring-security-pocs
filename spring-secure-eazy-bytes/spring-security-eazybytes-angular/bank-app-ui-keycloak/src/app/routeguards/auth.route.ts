import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { User } from '../model/user.model';
import { KeycloakProfile } from 'keycloak-js';

@Injectable({
    providedIn: 'root',
  })
  export class AuthKeyClockGuard extends KeycloakAuthGuard {

    user = new User();
    public userProfile: KeycloakProfile | null = null;
    constructor(
      protected override readonly router: Router,
      protected readonly keycloak: KeycloakService
    ) {
      super(router, keycloak);
    }

    public async isAccessAllowed(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
      ) {
        // Force the user to log in if currently unauthenticated.
        if (!this.authenticated) {
          await this.keycloak.login({
            redirectUri: window.location.origin + state.url,
          });
        }else{
            this.userProfile = await this.keycloak.loadUserProfile();
            this.user.authStatus = 'AUTH';
            this.user.name = this.userProfile.firstName || "";
            this.user.email = this.userProfile.email || "";
            window.sessionStorage.setItem("userdetails",JSON.stringify(this.user));
        }
    
        // Get the roles required from the route.
        const requiredRoles = route.data["roles"];
    
        // Allow the user to to proceed if no additional roles are required to access the route.
        if (!(requiredRoles instanceof Array) || requiredRoles.length === 0) {
          return true;
        }
    
        // Allow the user to proceed if all the required roles are present.
        return requiredRoles.some((role) => this.roles.includes(role));
      }

  }

// @Injectable()
// export class AuthActivateRouteGuard {
//     user = new User();
    
//     constructor(private router: Router){

//     }

//     canActivate(route:ActivatedRouteSnapshot, state:RouterStateSnapshot){
//         if(sessionStorage.getItem('userdetails')){
//             this.user = JSON.parse(sessionStorage.getItem('userdetails')!);
//         }
//         if(this.user.email.length===0){
//             this.router.navigate(['login']);
//         }
//         return this.user.email.length!==0?true:false;
//     }

// }

// export const AuthGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
//     return inject(AuthActivateRouteGuard).canActivate(next, state);
//   }