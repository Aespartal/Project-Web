import { Component, Input } from "@angular/core";

@Component({
  selector: 'jhi-bar-title',
  templateUrl: './bar-title.component.html',
  styleUrls: ['./bar-title.component.scss'],
})
export class BarTitleComponent {

  @Input() title!: string;

  constructor() {
    // Empty constructor
  }
}
