export interface IUser {
  id?: number | null;
  login?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  activated?: boolean | null;
  langKey?: string | null;
  authorities?: string[] | null;
  createdBy?: string | null;
  createdDate?: Date | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: Date | null;
}

export class User implements IUser {
  constructor(
    public id?: number | null,
    public login?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public activated?: boolean | null,
    public langKey?: string | null,
    public authorities?: string[] | null,
    public createdBy?: string | null,
    public createdDate?: Date | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: Date | null
  ) {
    this.activated = this.activated ?? false;
  }
}
