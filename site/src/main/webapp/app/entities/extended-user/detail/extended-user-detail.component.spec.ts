import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExtendedUserDetailComponent } from './extended-user-detail.component';

describe('ExtendedUser Management Detail Component', () => {
  let comp: ExtendedUserDetailComponent;
  let fixture: ComponentFixture<ExtendedUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExtendedUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ extendedUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ExtendedUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExtendedUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load extendedUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.extendedUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
