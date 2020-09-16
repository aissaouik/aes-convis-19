import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ConvidTestModule } from '../../../test.module';
import { BannerUpdateComponent } from 'app/entities/banner/banner-update.component';
import { BannerService } from 'app/entities/banner/banner.service';
import { Banner } from 'app/shared/model/banner.model';

describe('Component Tests', () => {
  describe('Banner Management Update Component', () => {
    let comp: BannerUpdateComponent;
    let fixture: ComponentFixture<BannerUpdateComponent>;
    let service: BannerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ConvidTestModule],
        declarations: [BannerUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BannerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BannerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BannerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Banner(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Banner();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
