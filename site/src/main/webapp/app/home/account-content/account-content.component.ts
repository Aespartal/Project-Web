import { Component, Input } from "@angular/core";
import { IExtendedUser } from "app/entities/extended-user/extended-user.model";

@Component({
  selector: 'jhi-account-content',
  templateUrl: './account-content.component.html',
  styleUrls: ['./account-content.component.scss'],
})
export class AccountContentComponent {

  @Input() extendedUser!: IExtendedUser;

  constructor() {
    // Empty constructor
  }


}
