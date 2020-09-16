import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBanner, Banner } from 'app/shared/model/banner.model';
import { BannerService } from './banner.service';

@Component({
  selector: 'aes-banner-update',
  templateUrl: './banner-update.component.html',
})
export class BannerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    content: [null, [Validators.required]],
    frequency: [null, [Validators.required]],
  });

  constructor(protected bannerService: BannerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banner }) => {
      this.updateForm(banner);
    });
  }

  updateForm(banner: IBanner): void {
    this.editForm.patchValue({
      id: banner.id,
      title: banner.title,
      content: banner.content,
      frequency: banner.frequency,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const banner = this.createFromForm();
    if (banner.id !== undefined) {
      this.subscribeToSaveResponse(this.bannerService.update(banner));
    } else {
      this.subscribeToSaveResponse(this.bannerService.create(banner));
    }
  }

  private createFromForm(): IBanner {
    return {
      ...new Banner(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      frequency: this.editForm.get(['frequency'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBanner>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
