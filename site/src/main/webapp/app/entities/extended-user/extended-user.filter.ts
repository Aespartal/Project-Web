export class ExtendedUserFilter {
  id: number | null = null;
  login: string | null = null;
  firstName: string | null = null;
  email: string | null = null;
  authorityName: string | null = null;
  activated: boolean | null = null;

  map: Map<string, any> = new Map<string, any>();

  constructor(
    id?: number,
    login?: string,
    firstName?: string,
    email?: string,
    authorityName?: string,
    activated?: boolean
    ) {
    this.login = login ?? null;
    this.firstName = firstName ?? null;
    this.email = email ?? null;
    this.authorityName = authorityName ?? null;
    this.activated = activated ?? null;
  }

  toMap(): any {
    if (this.id != null) {
      this.map.set('id.equals', this.id);
    }
    if (this.login != null) {
      this.map.set('login.contains', this.login);
    }
    if (this.firstName != null) {
      this.map.set('firstName.contains', this.firstName);
    }
    if (this.email != null) {
      this.map.set('email.contains', this.email);
    }
    if (this.authorityName != null) {
      this.map.set('authorityName.equals', this.authorityName);
    }
    if (this.activated != null) {
      this.map.set('activated.equals', this.activated);
    }

    return this.map;
  }

  clear(): void {
    this.map = new Map<string, any>();
  }

  addFilter(key: string, value: string): void {
    if (key !== '' && value !== '') {
      this.toMap().set(key, value);
    }
  }
}
