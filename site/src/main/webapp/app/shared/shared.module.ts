import { NgModule } from '@angular/core';

import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { FilterComponent } from './filter/filter.component';
import { ImageCardComponent } from './components/image-card/image-card.component';
import { BarTitleComponent } from './components/bar-title/bar-title.component';
import { NotificationTypeIconDirective } from './directive/notification-type-icon.directive';
import { MessageEmptyComponent } from './components/message-empty/message-empty.component';
import { FavButtonComponent } from './components/fav-button/fav-button.component';

import { StepsModule } from 'primeng/steps';
import { CommentButtonComponent } from './components/comment-button/comment-button.component';

import { ImageCropperModule } from 'ngx-image-cropper';

@NgModule({
  imports: [SharedLibsModule, StepsModule, ImageCropperModule],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    ImageCardComponent,
    BarTitleComponent,
    NotificationTypeIconDirective,
    MessageEmptyComponent,
    FavButtonComponent,
    CommentButtonComponent
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    ImageCardComponent,
    BarTitleComponent,
    NotificationTypeIconDirective,
    MessageEmptyComponent,
    FavButtonComponent,
    StepsModule,
    CommentButtonComponent,
    ImageCropperModule
  ],
})
export class SharedModule {}
