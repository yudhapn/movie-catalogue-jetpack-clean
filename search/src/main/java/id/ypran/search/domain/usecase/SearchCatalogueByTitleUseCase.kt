package id.ypran.search.domain.usecase

import id.ypran.search.domain.repository.SearchCatalogueRepository

class SearchCatalogueByTitleUseCase(private val repository: SearchCatalogueRepository) {
    fun execute(query: String) = repository.searchCatalogueByTitle(query)
}