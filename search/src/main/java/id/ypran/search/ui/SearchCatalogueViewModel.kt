package id.ypran.search.ui

import androidx.lifecycle.ViewModel
import id.ypran.search.domain.usecase.SearchCatalogueByTitleUseCase

class SearchCatalogueViewModel(
    private val searchCatalogueByTitleUseCase: SearchCatalogueByTitleUseCase
) : ViewModel() {
    fun searchCatalogue(query: String) = searchCatalogueByTitleUseCase.execute(query)
}