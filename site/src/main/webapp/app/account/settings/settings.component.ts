import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LANGUAGES } from 'app/config/language.constants';

const initialAccount: Account = {} as Account;

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  success = false;
  languages = LANGUAGES;

  settingsForm = new FormGroup({
    firstName: new FormControl(initialAccount.firstName, {
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)],
    }),
    lastName: new FormControl(initialAccount.lastName, {
      validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)],
    }),
    email: new FormControl(initialAccount.email, {
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    langKey: new FormControl(initialAccount.langKey),

    activated: new FormControl(initialAccount.activated),
    authorities: new FormControl(initialAccount.authorities),
    imageUrl: new FormControl(initialAccount.imageUrl),
    login: new FormControl(initialAccount.login),
  });

  constructor(private accountService: AccountService, private translateService: TranslateService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account?.user) {
        this.settingsForm.patchValue(account.user);
      }
    });
  }

  save(): void {
    this.success = false;

    const account = this.settingsForm.getRawValue();
    // this.accountService.save(account).subscribe(() => {
    //   this.success = true;

    //   this.accountService.authenticate(account);

    //   if (account.langKey !== this.translateService.currentLang) {
    //     this.translateService.use(account.langKey);
    //   }
    // });
  }
}
