package com.irakliy.util

import androidx.appcompat.widget.SearchView
import com.irakliy.util.RxSearchObservable.fromView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Util for search view
 *
 *  Call [fromView] to get [Observable]
 */
object RxSearchObservable {

    fun fromView(searchView: SearchView): Observable<String> {
        val subject = PublishSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                subject.onNext(newText)
                return true
            }
        })
        return subject
    }

}