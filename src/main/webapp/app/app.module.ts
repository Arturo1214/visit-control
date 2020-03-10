import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { VisitSharedModule } from 'app/shared/shared.module';
import { VisitCoreModule } from 'app/core/core.module';
import { VisitAppRoutingModule } from './app-routing.module';
import { VisitHomeModule } from './home/home.module';
import { VisitEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    VisitSharedModule,
    VisitCoreModule,
    VisitHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    VisitEntityModule,
    VisitAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class VisitAppModule {}
