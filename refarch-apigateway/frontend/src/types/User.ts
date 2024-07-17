class User {
  sub = "";

  // see https://wiki.muenchen.de/betriebshandbuch/wiki/Red_Hat_Single_Sign-On_(Keycloak)#Scopes
  // LHM
  displayName = "";
  surname = "";
  telephoneNumber = "";
  email = "";
  username = "";
  givenname = "";
  department = "";
  lhmObjectID = "";
  // LHM_Extended
  preferred_username = "";
  memberof: string[] = [];
  user_roles: string[] = [];
  authorities: string[] = [];
}

export function UserLocalDevelopment(): User {
  const u = new User();
  u.username = "Local Development User";
  u.displayName = "Local Development User";
  u.authorities = [
    // todo add authorities
  ];
  u.user_roles = [
    // todo add user roles
  ];
  return u;
}

export default User;
