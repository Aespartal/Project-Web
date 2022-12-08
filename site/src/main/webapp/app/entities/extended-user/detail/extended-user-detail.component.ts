import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExtendedUser } from '../extended-user.model';

@Component({
  selector: 'jhi-extended-user-detail',
  templateUrl: './extended-user-detail.component.html',
})
export class ExtendedUserDetailComponent implements OnInit {
  extendedUser: IExtendedUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extendedUser }) => {
      this.extendedUser = extendedUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
